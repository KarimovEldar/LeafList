package com.example.leaflist.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.leaflist.R
import com.example.leaflist.SharedViewModel
import com.example.leaflist.data.model.ToDoData
import com.example.leaflist.data.viewmodel.ToDoViewModel
import com.example.leaflist.databinding.FragmentListBinding
import com.example.leaflist.fragments.list.adapter.ListAdapter
import com.example.leaflist.utils.observeOnce
import com.google.android.material.snackbar.Snackbar
class ListFragment : Fragment() , SearchView.OnQueryTextListener{
    private lateinit var binding: FragmentListBinding
    private lateinit var toDoViewModel: ToDoViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private val adapter by lazy{
        ListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toDoViewModel = ViewModelProvider(requireActivity())[ToDoViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater,container,false)

        setUpRecyclerView()

        toDoViewModel.readDatabase.observe(viewLifecycleOwner){
            sharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
            //Recyclerview animation
            binding.recyclerView.scheduleLayoutAnimation()
        }
        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner){
            showEmptyDatabaseViews(it)
        }

        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu,menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@ListFragment)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete_all -> {
                        deleteAllData()
                    }

                    R.id.menu_priority_low -> {
                        toDoViewModel.sortByLowPriority.observe(viewLifecycleOwner) {
                            adapter.setData(it)
                        }
                    }

                    R.id.menu_priority_high -> {
                        toDoViewModel.sortByHighPriority.observe(viewLifecycleOwner) {
                            adapter.setData(it)
                        }
                    }
                }
                return true
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpRecyclerView(){
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)

        //Swipe to delete
        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.toDoList[viewHolder.adapterPosition]
                // Delete Item
                toDoViewModel.deleteData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore Deleted Item
                restoreDeletedData(viewHolder.itemView,deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view:View, deletedData: ToDoData){
        val snackbar = Snackbar.make(
            view,
            "Deleted '${deletedData.title}'",
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction("Undo"){
            toDoViewModel.insertData(deletedData)
        }
        snackbar.show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!=null){
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun searchThroughDatabase(query:String){
        val searchQuery = "%$query%"
        toDoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner){
            adapter.setData(it)
        }
    }

    private fun showEmptyDatabaseViews(emptyDatabase:Boolean){
        if (emptyDatabase){
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        }else{
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    private fun deleteAllData(){
        val builder= AlertDialog.Builder(requireContext())

        builder.setPositiveButton("Yes"){_,_->
            toDoViewModel.deleteAllData()
            Toast.makeText(requireContext(),"Successfully Removed Everything!", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove everything?")
        builder.create().show()
    }

}
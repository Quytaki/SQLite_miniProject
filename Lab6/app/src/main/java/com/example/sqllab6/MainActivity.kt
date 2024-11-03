package com.example.sqllab6

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private lateinit var productDatabaseHelper: ProductDatabaseHelper
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRecyclerView: RecyclerView

    private fun onEditProduct(product: Product) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_product, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editProductName)
        val priceEditText = dialogView.findViewById<EditText>(R.id.editProductPrice)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.editProductDescription)

        // Điền thông tin sản phẩm vào dialog
        nameEditText.setText(product.name)
        priceEditText.setText(product.price.toString())
        descriptionEditText.setText(product.description)

        AlertDialog.Builder(this)
            .setTitle("Sửa Sản Phẩm")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val newName = nameEditText.text.toString()
                val newPrice = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
                val newDescription = descriptionEditText.text.toString()
                if (newName.isNotBlank() && newPrice > 0) {
                    productDatabaseHelper.updateProduct(product.id, newName, newPrice, newDescription)
                    loadProducts()  // Cập nhật danh sách sản phẩm
                } else {
                    Toast.makeText(this, "Vui lòng nhập thông tin hợp lệ!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun onDeleteProduct(product: Product) {
        productDatabaseHelper.deleteProduct(product.id)
        loadProducts()  // Cập nhật danh sách sản phẩm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        productDatabaseHelper = ProductDatabaseHelper(this)
        productRecyclerView = findViewById(R.id.productRecyclerView)

        loadProducts()

        val addProductButton: Button = findViewById(R.id.addProductButton)
        addProductButton.setOnClickListener {
            showAddProductDialog()
        }
    }
    private fun loadProducts() {
        val products = productDatabaseHelper.getAllProducts()
        productAdapter = ProductAdapter(products, ::onEditProduct, ::onDeleteProduct)
        productRecyclerView.layoutManager = LinearLayoutManager(this)
        productRecyclerView.adapter = productAdapter
    }

    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_product, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editProductName)
        val priceEditText = dialogView.findViewById<EditText>(R.id.editProductPrice)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.editProductDescription)

        AlertDialog.Builder(this)
            .setTitle("Thêm Sản Phẩm")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val name = nameEditText.text.toString()
                val price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
                val description = descriptionEditText.text.toString()
                if (name.isNotBlank() && price > 0) {  // Kiểm tra thông tin
                    productDatabaseHelper.addProduct(name, price, description)
                    loadProducts()
                } else {
                    Toast.makeText(this, "Vui lòng nhập thông tin hợp lệ!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

}

package com.example.shoppinglisttwooseven

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglisttwooseven.ui.theme.Green40

@Composable
fun MainScreen(){

    var index by remember {
        mutableIntStateOf(1)
    }

    var listOfShoppingItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }

    var isAdding by remember {
        mutableStateOf(false)
    }

    var itemToEdit by remember {
        mutableStateOf<ShoppingItem?>(null)
    }

    if (isAdding){
        AddDialog(
            index = index,
          onDismissRequest = {
              isAdding = false
          },
            onAddClicked = {
                listOfShoppingItems += it
                index++
                isAdding = false
            }
        )
    }

    itemToEdit?.let { shoppingItem ->
        EditDialog(
            shoppingItem = shoppingItem,
            onDismissRequest = { itemToEdit = null },
            onEditCompleted = { name, description, quantity ->
                listOfShoppingItems = listOfShoppingItems.map {
                    if (it.id == shoppingItem.id) {
                        it.copy(title = name, description = description, quantity = quantity)
                    } else {
                        it
                    }
                }
                itemToEdit = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(5.dp)
                .width(60.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green40
            ),
            onClick = {
                isAdding = true
            }
        ) {
            Text("+",fontWeight = FontWeight.Bold,fontSize = 24.sp)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(listOfShoppingItems) { element ->
                ShoppingItemCard(
                    shoppingItem = element,
                    onEditClicked = {
                        itemToEdit = element
                    },
                    onDeleteClicked = { listOfShoppingItems -= it }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(index: Int, onDismissRequest: () -> Unit, onAddClicked: (ShoppingItem) -> Unit){

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var quantity by remember {
        mutableIntStateOf(1)
    }

    BasicAlertDialog(
        modifier = Modifier.background(
            color = Color.White,
            shape = RoundedCornerShape(5.dp)
        ),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ),
        ){
            TextField(
                placeholder = { Text("Title") },
                value = title,
                onValueChange = {
                    title = it
                }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Description") },
                value = description,
                onValueChange = {
                    description = it
                }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                placeholder = { Text("Description") },
                value = quantity.toString(),
                onValueChange = {
                    quantity = it.toInt()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green40
                    ),
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Отмена")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green40
                    ),
                    onClick = {
                        onAddClicked(
                            ShoppingItem(
                                id = index,
                                title = title,
                                description = description,
                                quantity = quantity
                            )
                        )
                        onDismissRequest()
                    }
                ) {
                    Text("Добавить")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(shoppingItem: ShoppingItem,onDismissRequest: () -> Unit, onEditCompleted: (String, String, Int) -> Unit) {

    var editedName by remember {
        mutableStateOf(shoppingItem.title)
    }
    var editedDescription by remember {
        mutableStateOf(shoppingItem.description)
    }
    var editedQuantity by remember {
        mutableIntStateOf(shoppingItem.quantity)
    }

    BasicAlertDialog(
        modifier = Modifier.background(
        color = Color.White,
        shape = RoundedCornerShape(5.dp)
    ),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
        ) {
            TextField(
                value = editedName,
                onValueChange = { editedName = it },
                placeholder = { Text("Title") }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                value = editedDescription,
                onValueChange = { editedDescription = it },
                placeholder = { Text("Description") }
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                value = editedQuantity.toString(),
                onValueChange = {
                    editedQuantity = it.toIntOrNull() ?: 0
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Description") }
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = Green40)
                ) {
                    Text("Отмена")
                }
                Button(
                    onClick = {
                        onEditCompleted(editedName, editedDescription, editedQuantity)
                        onDismissRequest()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Green40)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}

@Composable
fun ShoppingItemCard(shoppingItem: ShoppingItem, onEditClicked: () -> Unit, onDeleteClicked: (ShoppingItem) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = shoppingItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = shoppingItem.description,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(0.4f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Quantity:",
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = shoppingItem.quantity.toString(),
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Column {
                IconButton(onClick = onEditClicked) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }
                IconButton(onClick = { onDeleteClicked(shoppingItem) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(){

}
package com.example.smarthelmetapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddContactScreen() {
    val primaryColor = Color(0xFF1B3C53)

    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var relation by remember { mutableStateOf("") }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primaryColor,
        unfocusedBorderColor = Color.Gray,
        cursorColor = primaryColor,
        focusedLabelColor = primaryColor,
        unfocusedLabelColor = primaryColor
    )

    val inputTextStyle = TextStyle(color = primaryColor) // Input text color

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        // Page Title
        Text(
            text = "Add Contact",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Name Input ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                placeholder = { Text("John Doe") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = primaryColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                colors = fieldColors,
                textStyle = inputTextStyle
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Contact Number Input ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                label = { Text("Contact Number") },
                placeholder = { Text("0771234567") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = primaryColor) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                colors = fieldColors,
                textStyle = inputTextStyle
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Relationship Input ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            OutlinedTextField(
                value = relation,
                onValueChange = { relation = it },
                label = { Text("Relationship") },
                placeholder = { Text("Father, Mother, Friend") },
                leadingIcon = { Icon(Icons.Default.Group, contentDescription = null, tint = primaryColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                colors = fieldColors,
                textStyle = inputTextStyle
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ---------- Save Button ----------
        Button(
            onClick = { /* TODO: Save contact */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "Save Contact",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

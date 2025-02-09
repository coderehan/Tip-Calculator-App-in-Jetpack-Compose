package com.rehan.tipcalculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TipCalculator() {

    val amount = remember {
        mutableStateOf("")
    }
    val personCounter = remember {
        mutableStateOf(1)   // Initially it will be 1 on the screen then we can do increment
    }
    val tipPercentage = remember {
        mutableStateOf(0f)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TotalHeader(
            amount = getTotalHeaderAmount(
                amount.value,
                personCounter = personCounter.value,
                tipPercentage = tipPercentage.value
            )
        )
        UserInputArea(amount = amount.value, amountChange = {
            amount.value = it
        }, personCounter = personCounter.value, onAddOrReducePerson = {
            if (it < 0) {
                if (personCounter.value != 1) {
                    personCounter.value--
                }
            } else {
                personCounter.value++
            }
        }, tipPercentage.value, {
            tipPercentage.value = it
        })
    }
}

@Composable
fun TotalHeader(amount: String) {   // We will set the amount to 0 initially
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = colorResource(id = R.color.teal_200),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Per Person",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$ $amount",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }   // Column close
    } // Surface close
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserInputArea(
    amount: String,
    amountChange: (String) -> Unit,
    personCounter: Int,
    onAddOrReducePerson: (Int) -> Unit,
    tipPercentage: Float,
    tipPercentageChange: (Float) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = amount,
                onValueChange = { amountChange.invoke(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Enter your Amount") },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )

            if (amount.isNotBlank()) {
                // We will show this only when user amount is not empty
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Split", style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.fillMaxWidth(.50f))
                    CustomButton(imageVector = Icons.Default.KeyboardArrowUp) {
                        onAddOrReducePerson.invoke(1)
                    }
                    Text(
                        text = "$personCounter",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(20.dp)
                    )
                    CustomButton(imageVector = Icons.Default.KeyboardArrowDown) {
                        onAddOrReducePerson.invoke(-1)
                    }

                }   // Row close

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Tip", style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.fillMaxWidth(.60f))
                    Text(
                        text = "$ ${getTipAmount(amount, tipPercentage)}",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(10.dp)
                    )
                } // Row close

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "$tipPercentage %", style = MaterialTheme.typography.body1)

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = tipPercentage,
                    onValueChange = {
                        tipPercentageChange.invoke(it)
                    },
                    valueRange = 0f..100f,
                    steps = 5,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                )
            }
        }   // Column close
    }   // Surface close
}

@Composable
fun CustomButton(imageVector: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
            .clickable {
                onClick.invoke()
            },
        shape = CircleShape
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(4.dp)

        )
    }
}
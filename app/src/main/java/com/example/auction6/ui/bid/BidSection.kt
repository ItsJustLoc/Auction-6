package com.example.auction6.ui.bid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment


@Composable
fun BidSection(currentHighestBid: Double) {

    //Create the input for the bid but make it changable.

    var bidInput by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }




    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text("Current Highest Bid: $currentHighestBid")
    Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            //copy the value into value
            value = bidInput,
            //whenever the user types, initiate it then update bidInput
            onValueChange = { bidInput = it },
            label = { Text("Enter your bid") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        //Bidding logic has to be inserted here:
        //like check if new bid is less the current highest and so on.
        Button(
            onClick = {
                // 1. convert bidInput into a number
                val bidNumber = bidInput.toDoubleOrNull()
                // 2. if invalid number -> resultMessage = "Please enter a valid number"
                if (bidNumber == null) {
                    // show "invalid number"
                    resultMessage = "Please enter a Valid Number"
                } else {
                    // continue with 5% rule
                    // 3. otherwise calculate minimum allowed bid
                    val minimumBid = currentHighestBid * 1.05

                    // 4. compare entered bid against minimum
                    if (bidNumber >= minimumBid){
                        resultMessage = "Valid Bid"
                    }
                    else {
                        // 5. update resultMessage
                        //number should be rounded off to 2 decimal spots
                        resultMessage = "Invalid Bid! Bid must be at least %.2f".format(minimumBid)
                    }
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Bid")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = resultMessage)
    }
}
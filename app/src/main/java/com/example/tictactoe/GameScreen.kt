package com.example.tictactoe

import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.style.TextAlign


@Composable

fun GameScreen() {

// 사용자(X)의 승리 횟수
    var xWins by remember { mutableStateOf(0) }
// 상대방(O)의 승리 횟수
    var oWins by remember { mutableStateOf(0) }
// 무승부 횟수
    var draws by remember { mutableStateOf(0) }
    var gameState by remember { mutableStateOf(GameState()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // ⬅️ 중앙 정렬 추가
    ) {
        Text(
            text = "by [차수민] - Jetpack Compose Project",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF333333))
                .padding(vertical = 10.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

        val headerText = when {
            gameState.winner != null -> "${gameState.winner!!.symbol} 승리!"
            gameState.isGameOver -> "무승부!"

            else -> "${gameState.currentPlayer.symbol} 차례"
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = "X Wins: $xWins", fontWeight = FontWeight.Bold, color = Color.Blue)
            Text(text = "Draws: $draws", color = Color.Gray)
            Text(text = "O Wins: $oWins", fontWeight = FontWeight.Bold, color = Color.Red)
        }


        Text(
            text = headerText,
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            color = if (gameState.isGameOver && gameState.winner == null) Color.Black else (gameState.winner ?: gameState.currentPlayer).color
        )

        Spacer(Modifier.height(48.dp)) //

        // 2. 게임판 3x3 Grid
        Column(modifier = Modifier.border(BorderStroke(3.dp, Color.Black))) {
            gameState.board.forEachIndexed { row, boardRow ->
                Row {
                    boardRow.forEachIndexed { col, cell ->

                        val isWinningCell = gameState.winningLine?.contains(Pair(row, col)) == true

                        Cell(
                            player = cell,
                            onClick = {
                                if (!gameState.isGameOver && cell == null) {
                                    gameState = makeMove(gameState, row, col)
                                }
                            },

                            isWinningCell = isWinningCell
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(48.dp)) //

        // 3. 재시작 버튼
        if (gameState.isGameOver) {
            Button(onClick = { gameState = GameState() }) { Text("게임 재시작", fontSize = 24.sp) } // ⬅️ 버튼 텍스트 확대
        }
    }
}

@Composable
fun Cell(player: Player?, onClick: () -> Unit, isWinningCell: Boolean) {
    // 승리 셀인 경우 배경을 밝은 노란색으로 설정 (추가된 기능)
    val backgroundColor = if (isWinningCell) Color(0xFFFFF176) else Color.White

    Box(
        modifier = Modifier
            .size(100.dp)
            .border(BorderStroke(1.dp, Color.Gray))
            .clickable(onClick = onClick)

            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (player != null) {
            Text(
                text = player.symbol, // O 또는 X
                color = player.color,
                fontSize = 72.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement // ⬅️ 추가
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.style.TextAlign


@Composable

fun GameScreen() {
    // [사용자 정보 바] 바로 아래 또는 Composable 함수 시작 부분에 추가

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
                .fillMaxWidth() // 가로 전체 채우기
                .background(Color(0xFF333333)) // 어두운 배경색
                .padding(vertical = 10.dp), // 상하 패딩 추가
            color = Color.White,
            textAlign = TextAlign.Center, // 텍스트 중앙 정렬
            style = MaterialTheme.typography.bodySmall // 작은 글꼴 사용 (Material 3 기준)
        )

        val headerText = when {
            gameState.winner != null -> "${gameState.winner!!.symbol} 승리!"
            gameState.isGameOver -> "무승부!"
            // 텍스트에서 '빨강'/'파랑' 대신 심볼만 표시하도록 단순화
            else -> "${gameState.currentPlayer.symbol} 차례"
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround // 요소들을 좌우로 분산 배치
        ) {
            Text(text = "X Wins: $xWins", fontWeight = FontWeight.Bold, color = Color.Blue)
            Text(text = "Draws: $draws", color = Color.Gray)
            Text(text = "O Wins: $oWins", fontWeight = FontWeight.Bold, color = Color.Red)
        }

        // 텍스트 크기 확대 및 중앙에 표시되도록 배치
        Text(
            text = headerText,
            fontSize = 36.sp, // ⬅️ 크기 확대
            fontWeight = FontWeight.Black, // ⬅️ 굵게
            color = if (gameState.isGameOver && gameState.winner == null) Color.Black else (gameState.winner ?: gameState.currentPlayer).color
        )

        Spacer(Modifier.height(48.dp)) // ⬅️ 보드와의 간격 확대

        // 2. 게임판 3x3 Grid
        Column(modifier = Modifier.border(BorderStroke(3.dp, Color.Black))) {
            gameState.board.forEachIndexed { row, boardRow ->
                Row {
                    boardRow.forEachIndexed { col, cell ->
                        // 승리 라인에 있는 셀인지 확인
                        val isWinningCell = gameState.winningLine?.contains(Pair(row, col)) == true

                        Cell(
                            player = cell,
                            onClick = {
                                if (!gameState.isGameOver && cell == null) {
                                    gameState = makeMove(gameState, row, col)
                                }
                            },
                            // ⬅️ 승리한 셀에는 강조 배경색 적용
                            isWinningCell = isWinningCell
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(48.dp)) // ⬅️ 버튼과의 간격 확대

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
            .size(100.dp) // ⬅️ 셀 크기 확대
            .border(BorderStroke(1.dp, Color.Gray))
            .clickable(onClick = onClick)
            // ⬅️ 심볼 배경색은 흰색/노란색으로 유지
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (player != null) {
            Text(
                text = player.symbol, // O 또는 X
                color = player.color, // ⬅️ 심볼 색상을 플레이어 색상(빨강/파랑)으로 설정
                fontSize = 72.sp, // ⬅️ 심볼 크기를 매우 크게 확대 (직관적)
                fontWeight = FontWeight.Black // ⬅️ 매우 굵게
            )
        }
    }
}

// ... (Preview 코드는 수정하지 않고 그대로 유지)
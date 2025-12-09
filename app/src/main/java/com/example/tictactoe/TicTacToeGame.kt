package com.example.tictactoe

import androidx.compose.ui.graphics.Color

// 승리 라인 저장을 위한 타입 정의 (행, 열의 쌍 리스트) ⬅️ 추가
typealias WinningLine = List<Pair<Int, Int>>

// 1. 플레이어 정의
enum class Player(val color: Color, val symbol: String) {
    // 플레이어 색상 코드를 직관적으로 변경 (UI 코드와 일치)
    RED(Color(0xFFE53935), "O"),    // 빨강 O
    BLUE(Color(0xFF1E88E5), "X");   // 파랑 X

    fun next(): Player = if (this == RED) BLUE else RED
}

typealias Board = List<List<Player?>>

// 2. 게임 상태 (모든 데이터)
data class GameState(
    val board: Board = List(3) { List(3) { null } },
    val currentPlayer: Player = Player.RED, // 1P(RED)가 먼저 시작
    val winner: Player? = null,
    val isGameOver: Boolean = false,
    val winningLine: WinningLine? = null // ⬅️ WinningLine 타입 사용
)

// 3-A. 승리 라인 좌표를 반환하는 함수 ⬅️ 추가
fun getWinningLine(board: Board): WinningLine? {
    val lines: List<WinningLine> = listOf(
        // 가로줄 (Rows)
        listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
        listOf(Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        listOf(Pair(2, 0), Pair(2, 1), Pair(2, 2)),
        // 세로줄 (Columns)
        listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0)),
        listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1)),
        listOf(Pair(0, 2), Pair(1, 2), Pair(2, 2)),
        // 대각선 (Diagonals)
        listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)),
        listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
    )

    for (coords in lines) {
        val player = board[coords[0].first][coords[0].second]
        // 첫 번째 셀이 비어있지 않고, 해당 라인의 모든 셀이 동일한 플레이어라면 승리
        if (player != null && coords.all { board[it.first][it.second] == player }) {
            return coords // 승리 라인 좌표 반환
        }
    }
    return null
}

// 3-B. 승패 체크 로직 (기존 함수 재정의)
fun checkWinner(board: Board): Player? {
    val line = getWinningLine(board)
    // 승리 라인이 있다면 해당 라인의 첫 번째 셀 플레이어를 반환
    return if (line != null) board[line[0].first][line[0].second] else null
}

fun isBoardFull(board: Board): Boolean {
    return board.all { row -> row.all { cell -> cell != null } }
}

// 4. 움직임 처리 함수
fun makeMove(state: GameState, row: Int, col: Int): GameState {
    if (state.isGameOver || state.board[row][col] != null) return state

    // 새로운 보드 생성 (이동 적용)
    val newBoard = state.board.mapIndexed { r, boardRow ->
        if (r == row) {
            boardRow.mapIndexed { c, cell ->
                if (c == col) state.currentPlayer else cell
            }
        } else boardRow
    }

    // 다음 상태 결정
    val newWinner = checkWinner(newBoard)
    val winningLine = if (newWinner != null) getWinningLine(newBoard) else null
    val isGameDone = newWinner != null || isBoardFull(newBoard)
    val nextPlayer = if (isGameDone) state.currentPlayer else state.currentPlayer.next()

    return state.copy(
        board = newBoard,
        currentPlayer = nextPlayer,
        winner = newWinner,
        isGameOver = isGameDone,
        winningLine = winningLine
    )
}
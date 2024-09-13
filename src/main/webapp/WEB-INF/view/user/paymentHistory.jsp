<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>
<link href="/css/paymentHistory.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="sidebar">
		<h3>나의 계정</h3>
		<a href="/user/mypageAuth">개인정보 수정</a> <a href="/user/pointHistory">포인트 내역 조회</a> <a href="/user/paymentHistory">결제 내역</a> <a class="delete-btn"
			onclick="window.open('/user/deleteAccount', '_blank', 'width=800,height=500,resizable=no')">회원탈퇴</a>
	</div>
	<h2 style="text-align: center">결제 내역</h2>

	<div class="section button-group">
		<a href="#">결제 내역 조회</a> <a href="#">환불 내역 조회</a>
	</div>
	<div class="content">
		<table>
			<thead>
				<tr>
					<th>결제 ID</th>
					<th>주문 번호</th>
					<th>상품 이름</th>
					<th>결제 수단</th>
					<th>총 금액</th>
					<th>결제 요청 일시</th>
					<th>결제 승인 일시</th>
					<th>상태</th>
				</tr>
			</thead>
			<c:forEach var="payment" items="${payment}">
				<tbody>
					<tr>
						<td>${payment.id}</td>
						<td>${payment.orderId}</td>
						<td>${payment.orderName}</td>
						<td>${payment.method}</td>
						<td>${payment.amountToString(payment.totalAmount)}원</td>
						<td>${payment.requestedAt}</td>
						<td>${payment.approvedAt}</td>
						<c:choose>
							<c:when test="${payment.status == 'DONE'}">
								<td style="color: green">완료 <c:choose>
										<c:when test="${payment.cancelStatus == null}">
											<button style="margin-left: 7px" onclick="window.open('/user/paymentRefund/${payment.id}', '_blank', 'width=800,height=500,resizable=no')">환불 요청</button>
										</c:when>
									</c:choose></td>
							</c:when>
							<c:when test="${payment.status == 'CANCELED'}">
								<td style="color: red"> 취소 <button>환불 요청</button></td>
							</c:when>
						</c:choose>
					</tr>
				</tbody>
			</c:forEach>
		</table>

		<table>
			<thead>
				<tr>
					<th>결제 ID</th>
					<th>주문 번호</th>

					<th>취소 금액</th>
					<th>취소 일시</th>
					<th>취소 사유</th>
					<th>취소 상태</th>
				</tr>
			</thead>
			<c:forEach var="payment" items="${payment}">
				<tbody>
					<tr>
						<td>${payment.id}</td>
						<td>${payment.orderId}</td>
						<td>${payment.cancelAmount}</td>

						<c:choose>
							<c:when test="${payment.canceledAt != null}">
								<td>${payment.timestampToString2(payment.canceledAt)}</td>
							</c:when>
							<c:otherwise>
								<td>-</td>
							</c:otherwise>
						</c:choose>

						<td>${payment.cancelReason}</td>
						<c:choose>
							<c:when test="${payment.cancelStatus == 'REQUEST_CANCEL'}">
								<td style="color: orange">결제 취소 요청중</td>
							</c:when>
							<c:when test="${payment.cancelStatus == 'DONE'}">
								<td style="color: green">결제 취소 완료</td>
							</c:when>
							<c:otherwise>
								<td>-</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</tbody>
			</c:forEach>
		</table>
		<div>
</body>
</html>
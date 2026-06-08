<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="pt-br">
	<head>
		<%@include file="base-head.jsp"%>
	</head>
	<body>
		<%@include file="nav-menu.jsp"%>
			
		<div id="container" class="container-fluid">
			<h3 class="page-header">Adicionar Anime</h3>

			<form action="${pageContext.request.contextPath}/anime/${action}" method="POST">
				<input type="hidden" value="${anime.getId()}" name="animeId">
				<div class="row">
					<div class="form-group col-md-4">
						<label for="title">Título</label>
						<input type="text" class="form-control" id="title" name="title" 
							   autofocus="autofocus" placeholder="Nome do anime" 
							   required oninvalid="this.setCustomValidity('Por favor, informe o título.')"
							   oninput="setCustomValidity('')"
							   value="${anime.getTitle()}">
					</div>

					<div class="form-group col-md-3">
						<label for="releaseDate">Data de Lançamento</label>
						<fmt:formatDate value="${anime.getReleaseDate()}" pattern="yyyy-MM-dd" var="fmtDate"/>
						<input type="date" class="form-control" id="releaseDate" name="releaseDate" 
							   required oninvalid="this.setCustomValidity('Por favor, informe a data.')"
							   oninput="setCustomValidity('')"
							   value="${fmtDate}">
					</div>

					<div class="form-group col-md-2">
						<label for="episodes">Qtd. Episódios</label>
						<input type="number" class="form-control" id="episodes" name="episodes" 
							   min="1"
							   required oninvalid="this.setCustomValidity('Por favor, informe o número de episódios.')"
							   oninput="setCustomValidity('')"
							   value="${anime.getEpisodes()}">
					</div>
					
					<div class="form-group col-md-3">
						<label for="autor">Autor</label>
						<select id="autor" class="form-control selectpicker" name="autor" 
							    required oninvalid="this.setCustomValidity('Por favor, informe o autor.')"
							    oninput="setCustomValidity('')">
						  <option value="" disabled ${not empty anime ? "" : "selected"}>Selecione um autor</option>
						  <c:forEach var="user" items="${users}">
						  	<option value="${user.getId()}"  ${anime.getAutor().getId() == user.getId() ? "selected" : ""}>
						  		${user.getName()}
						  	</option>	
						  </c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="form-group col-md-12">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="isFinished" id="isFinished" value="true" ${anime.getIsFinished() ? 'checked' : ''}>
								Anime já finalizado?
							</label>
						</div>
					</div>
				</div>
				<hr />
				<div id="actions" class="row pull-right">
					<div class="col-md-12">
						<a href="${pageContext.request.contextPath}/animes" class="btn btn-default">Cancelar</a>
						<button type="submit" class="btn btn-primary">${not empty anime ? "Alterar Anime" : "Criar Anime"}</button>
					</div>
				</div>
			</form>
		</div>

		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</body>
</html>
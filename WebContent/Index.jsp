<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Model Definer</title>

<!-- bootsnipp drag&drop -->
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet" type="text/css" href="modify.css">
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<!-- mio. -->
<script src="script.js"></script>

</head>
<body>
	<form method="post" action="Pick" id="formSub"
		enctype="multipart/form-data">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group files">
						<label>Upload file modello</label> <input type="file"
							class="form-control" id="starter" multiple="" name="starter"
							required>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group files">
						<label>Upload file modello</label> <input type="file"
							class="form-control" id="ebaySheet" multiple="" name="ebaySheet"
							required>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label for="exampleInputEmail1">inserire le righe da
							saltare separate da -</label> <input type="text" class="form-control"
							id="daSaltare" name="daSaltare" placeholder="es. 3" name=""
							required>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label for="exampleInputEmail1">Nome del file generato</label> <input
							type="text" class="form-control" id="newFileName"
							name="newFileName" placeholder="es. caricamento tazza" required>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-2">
					<button type="submit" class="btn btn-success"
						onClick="return send()" value="Upload">Send</button>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
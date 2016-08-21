<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Registration page">
	<meta name="author" content="Team-Titans">
	<title>Registration Page</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.js"></script>  
	<link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>  
	<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/css/bootstrapValidator.min.css"/>
	<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"> </script>
	<style>
  body {
    padding-top: 10px;
  }
  .error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}
</style>
</head>
<body id="body">
    <div class="container">
		<div class = "row-fluid">	
		<h2>Registration Page</h2>		
			<form:form class = "form-horizontal" name='RegistrationPage' id="testform" action="register" method='POST'>				
				<div class = "form-group">
				
					<c:if test="${not empty error}">
					<div class="error">${error}</div>
					</c:if>
					
					<c:if test="${not empty success}">
					<div class="msg">${success}</div>
					</c:if>
					
					<label class="col-md-3 control-label">User Name</label>
					<div class="col-md-4">
						<input type="text" class="form-control" name="username" placeholder="Enter User Name" />
					</div>
					
				</div>		
                <div class= "form-group">
                    <label class="col-md-3 control-label">EmailId:</label>
                    <div class="col-md-4">
                        <input type="email" class="form-control" name="email" placeholder="Enter your Email-Id"/>
                    </div>
                </div>	
                <div class= "form-group">
				    <label class="col-md-3 control-label">Password:</label>
					<div class="col-md-4">
					    <input type="Password" class="form-control" name="password" placeholder="your password"/>
					</div>
				</div>		
                <div class= "form-group">
				    <label class="col-md-3 control-label">Confirm Password:</label>
					<div class="col-md-4">
					    <input type="Password" class="form-control" name="confirmPassword" placeholder="Confirm your password"/>
					</div>
				</div>				
            				
				<div class="form-group">
					<div class="col-md-9 col-md-offset-3">
						<button type="submit" class="btn btn-success">Register</button>
					</div>
				</div>
			</form:form>	
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
	 var validator = $("#testform").bootstrapValidator({
		feedbackIcons : {
			valid : "glyphicon glyphicon-ok",
			invalid : "glyphicon glyphicon-remove",
			validating : "glyphicon glyphicon-recycle"
		},
		fields: {
			username : {
				validators : {
					notEmpty : {
						message : "cannot be empty"
					}
				}
			},
			email : {
				validators : {
					notEmpty : {
						message : "Email-Id cannot be empty"
					}
				}
			}
		}
	 });
	});
$(document).ready(function() {
    $('#regexpForm').formValidation({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            password: {
                validators: {
                    regexp: {
                        regexp: /^[a-z\s]+$/i,
                        message: 'The password can consist of alphabetical characters and spaces only'
                    }
                }
            }
        }
    });
});
</script>
</html>
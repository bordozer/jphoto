<%@ taglib prefix="eco" uri="http://taglibs" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
	.toggler {
		position: absolute;
		left: 690px;
		top: 40px;
		width: 270px;
	}

	#effect {
		padding: 0.4em;
		position: relative;
	}

	#tooltipMessage {
	}

	#effect h3 {
		margin: 0;
		padding: 0.4em;
		text-align: center;
	}
</style>

<div class="toggler">
	<div id="effect" class="ui-widget-content ui-corner-all" style="display: none">
		<h3 class="ui-widget-header ui-corner-all">
			<div id="titleMessage"></div>
		</h3>
		<p>
			<div id="tooltipMessage"></div>
		</p>
	</div>
</div>
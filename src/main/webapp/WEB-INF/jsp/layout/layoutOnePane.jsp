<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<stripes:layout-definition>
		<div id="eBodyContainer">
		     <div id="eContent">
		     	<h1 class="eH1Sifr"><c:out value="${pageTitle}" /></h1>
		     	<stripes:layout-component name="top100"/>
		     	<div class="eColGroup eBt ePt">
					<div class="eColGroup">
						<div class="eCol2w75">
							<stripes:layout-component name="leftCol" />
						</div>
						<div class="eCol2w25">
							<stripes:layout-component name="mainCol" />							
						</div>
					
					</div>
				</div>
			</div>
		</div>
</stripes:layout-definition>
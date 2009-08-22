<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp" title="Welcome">
  <s:layout-component name="body">
    <script>
    	function init() {
    	  dwr.util.useLoadingMessage();
    	  //Tabs.init('tabList', 'tabContents');
    	  fillTable();
    	}
    	function fillTable() {
    		  JkController.getStatus(function(jkStatuses) {
    		    // Delete all the rows except for the "pattern" row
    		    dwr.util.removeAllRows("peoplebody", { filter:function(tr) {
    		      return (tr.id != "pattern");
    		    }});
    		    // Create a new set cloned from the pattern row
    		    var jkStatus, id;
    		    //people.sort(function(p1, p2) { return p1.name.localeCompare(p2.name); });
    		    for (var i = 0; i < jkStatuses.length; i++) {
    		      jkStatus = jkStatuses[i];
    		      id = person.id;
    		      dwr.util.cloneNode("pattern", { idSuffix:id });
    		      dwr.util.setValue("tableName" + id, jkStatus.name);
    		      dwr.util.setValue("tableSalary" + id, person.salary);
    		      dwr.util.setValue("tableAddress" + id, person.address);
    		      $("pattern" + id).style.display = ""; // officially we should use table-row, but IE prefers "" for some reason
    		      peopleCache[id] = person;
    		    }
    		  });
    	}
        function update() {
    	  var name = "localhost";
        	  //dwr.util.getValue("demoName");
    	  JkController.getStatus(name, function(data) {
    	    dwr.util.setValue("demoReply", data);
    	  });
    	}
    </script>
    <p>JK Status</p>
    <table border="1" class="rowed grey">
      <thead>
        <tr>
          <th>Person</th>
          <th>Salary</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody id="peoplebody">
        <tr id="pattern" style="display:none;">
          <td>
            <span id="tableName">Name</span><br/>
            <small>&nbsp;&nbsp;<span id="tableAddress">Address</span></small>
          </td>
          <td>$<span id="tableSalary">Salary</span></td>
          <td>
            <input id="edit" type="button" value="Edit" onclick="editClicked(this.id)"/>
            <input id="delete" type="button" value="Delete" onclick="deleteClicked(this.id)"/>
          </td>
        </tr>
      </tbody>
    </table>    
	<p>
	  Name:
	  <input type="text" id="demoName"/>
	  <input value="Enable" type="button" onclick="enable()"/>
	  <br/>
	  <input value="Disable" type="button" onclick="disable()"/>
	  <br/>
	  <input value="Update" type="button" onclick="update()"/>
	  <br/>
	  Reply: <span id="demoReply"></span>
	</p>
  </s:layout-component>
</s:layout-render>

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".user-toggle").forEach(toggle => {
        toggle.addEventListener("change", function() {
            const row = this.closest("tr");
            const userId = row.getAttribute("data-user-id");
            const active = this.checked;

            fetch(`/admin/users/${userId}/status`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: `active=${active}`
            })
            .then(response => {
                if (response.ok) {
                    const badge = row.querySelector(".status-badge");
                    if (active) {
                        badge.textContent = "Active";
                        badge.classList.remove("bg-secondary");
                        badge.classList.add("bg-success");
                    } else {
                        badge.textContent = "Inactive";
                        badge.classList.remove("bg-success");
                        badge.classList.add("bg-secondary");
                    }
                } else {
                    alert("Failed to update status.");
                    this.checked = !active; // rollback
                }
            })
            .catch(() => {
                alert("Error updating status.");
                this.checked = !active; // rollback
            });
        });
    });
});

function toggleSelectAll(selectAllCheckbox) {
	  const checkboxes = document.querySelectorAll('input[name="userIds"]');
	  checkboxes.forEach(cb => cb.checked = selectAllCheckbox.checked);
	}

	function deleteSelectedUsers() {
	  const selected = Array.from(document.querySelectorAll('input[name="userIds"]:checked'))
	                        .map(cb => cb.value);
	  
	  if (selected.length === 0) {
	    alert("Please select at least one user to delete.");
	    return;
	  }

	  if (!confirm("Are you sure you want to delete selected user(s)?")) return;

	  fetch("/admin/users/delete", {
	    method: "POST",
	    headers: {
	      "Content-Type": "application/json"
	    },
	    body: JSON.stringify(selected)
	  })
	  .then(res => {
	    if (!res.ok) throw new Error("Failed to delete users.");
	    window.location.reload(); // Refresh to reflect changes
	  })
	  .catch(err => alert(err.message));
	}

	  document.addEventListener('DOMContentLoaded', function () {
	    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
	    tooltipTriggerList.forEach(function (tooltipTriggerEl) {
	      new bootstrap.Tooltip(tooltipTriggerEl);
	    });
	  });
	
	// Search filter
	  document.getElementById("searchBox").addEventListener("keyup", function () {
	      const value = this.value.toLowerCase();
	      document.querySelectorAll("#userTableBody tr").forEach(row => {
	          row.style.display = row.textContent.toLowerCase().includes(value) ? "" : "none";
	      });
	  });	  

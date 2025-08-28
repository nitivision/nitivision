    $(document).ready(function () {
        let rowsPerPage = 10;
        let rows = $("#contactTable tr");
        let pagination = $("#pagination");

        function filterRows() {
            let searchValue = $("#searchBox").val().toLowerCase();
            let serviceValue = $("#serviceFilter").val().toLowerCase();

            rows.show().filter(function () {
                let text = $(this).text().toLowerCase();
                let service = $(this).find("td:eq(5)").text().toLowerCase();

                return !(text.indexOf(searchValue) > -1 &&
                         (serviceValue === "" || service === serviceValue));
            }).hide();

            paginate();
        }

        function paginate() {
            pagination.empty();
            let visibleRows = rows.filter(":visible");
            let rowsCount = visibleRows.length;
            let pageCount = Math.ceil(rowsCount / rowsPerPage);

            for (let i = 1; i <= pageCount; i++) {
                pagination.append(`<li class="page-item"><a class="page-link" href="#">${i}</a></li>`);
            }

            showPage(1, visibleRows);
            pagination.find("li:first").addClass("active");

            pagination.off("click").on("click", "a", function (e) {
                e.preventDefault();
                let page = parseInt($(this).text());
                showPage(page, visibleRows);
                pagination.find("li").removeClass("active");
                $(this).parent().addClass("active");
            });
        }

        function showPage(page, visibleRows) {
            let start = (page - 1) * rowsPerPage;
            let end = start + rowsPerPage;
            rows.hide();
            visibleRows.slice(start, end).show();
        }

        // Bind search + filter
        $("#searchBox, #serviceFilter").on("keyup change", filterRows);

        // Init
        filterRows();
    });


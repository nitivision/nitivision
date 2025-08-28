function toggleMenu() {
  const sideMenu = document.getElementById("sideMenu");
  const toggleBtn = document.querySelector(".menu-toggle");

  if (sideMenu.style.width === "250px") {
    sideMenu.style.width = "0";
    toggleBtn.classList.remove("active");
  } else {
    sideMenu.style.width = "250px";
    toggleBtn.classList.add("active");
  }
}

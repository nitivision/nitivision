/*function toggleMenu() {
  const menu = document.getElementById("sideMenu");
  menu.style.display = menu.style.display === "block" ? "none" : "block";
}

// Auto slider
let currentSlide = 0;
setInterval(() => {
  const slider = document.getElementById('slider');
  const slides = slider.children.length;
  currentSlide = (currentSlide + 1) % slides;
  slider.style.transform = `translateX(-${currentSlide * 100}%)`;
}, 5000);

// Load dynamic content
function loadContent(page, event) {
  if (event) event.preventDefault();
  fetch('content?page=' + page)
    .then(res => res.text())
    .then(data => {
      document.getElementById('content').innerHTML = data;
    })
    .catch(() => {
      document.getElementById('content').innerHTML = '<p>Failed to load content.</p>';
    });
}
*/
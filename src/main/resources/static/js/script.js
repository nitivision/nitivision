function toggleMenu() {
  const menu = document.getElementById("sideMenu");
  menu.style.display = menu.style.display === "block" ? "none" : "block";
}

var swiper = new Swiper(".mySwiperHorizontal", {
  slidesPerView: 5,
  spaceBetween: 5,
  loop: true,
  autoplay: {
    delay: 2500,
    disableOnInteraction: false,
  },
  navigation: {
    nextEl: ".swiper-button-next",
    prevEl: ".swiper-button-prev",
  },
  pagination: {
    el: ".swiper-pagination",
    clickable: true,
  },
  breakpoints: {
    1024: { slidesPerView: 5 },
    768: { slidesPerView: 3 },
    480: { slidesPerView: 2 },
    320: { slidesPerView: 1 },
  }
});

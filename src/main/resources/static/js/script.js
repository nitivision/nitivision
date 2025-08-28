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

function openModal(title, description, imageSrc) {
	  document.getElementById('modalTitle').textContent = title;
	  document.getElementById('modalDescription').textContent = description;
	  document.getElementById('modalImage').src = imageSrc;
	  document.getElementById('portfolioModal').style.display = 'flex';
	}

	function closeModal() {
	  document.getElementById('portfolioModal').style.display = 'none';
	}

	window.onclick = function(event) {
	  if (event.target === document.getElementById('portfolioModal')) {
	    closeModal();
	  }
	}
	
	function showSuccessModal() {
	    document.getElementById("successModal").style.display = "flex";
	}

	function closeSuccessModal() {
	    document.getElementById("successModal").style.display = "none";
	}

	// If Spring sends a successMessage, show modal
	window.onload = function() {
	    if ("[[${successMessage}]]" !== "" && "[[${successMessage}]]" !== "null") {
	        showSuccessModal();
	    }
	}


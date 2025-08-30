document.addEventListener("DOMContentLoaded", () => {
  let formSubmitted = false;
  const contactModal = new bootstrap.Modal(document.getElementById('contactModal'));

  // Auto-popup every 10s until submitted
  const interval = setInterval(() => {
    if (!formSubmitted) {
      contactModal.show();
    } else {
      clearInterval(interval);
    }
  }, 10000);

  // Form submission
  document.getElementById('contactForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const recaptchaResponse = grecaptcha.getResponse();
    if (recaptchaResponse.length === 0) {
      alert("Please verify that you are not a robot.");
      return;
    }
    const data = {
      name: this.name.value,
      email: this.email.value,
      phone: this.phone.value,
      location: this.location.value,
      service: this.service.value,
      message: this.message.value
    };

    fetch('/savepopup', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(r => {
      if (r.ok) {
        alert('Thanks! Your response has been saved.');
        formSubmitted = true;
        contactModal.hide();
      } else {
        alert('Error submitting form.');
      }
    });
  });
});

// Toggle menu
function toggleMenu() {
  const mobileMenu = document.getElementById("mobileMenu");
  const sideMenu = document.getElementById("sideMenu");

  if (window.innerWidth <= 768) {
    mobileMenu.style.display =
      mobileMenu.style.display === "block" ? "none" : "block";
    sideMenu.style.display = "none";
  } else {
    sideMenu.style.display =
      sideMenu.style.display === "block" ? "none" : "block";
    mobileMenu.style.display = "none";
  }
}

// Toggle dropdown in mobile menu
function toggleDropdown(button) {
  const content = button.nextElementSibling;
  const arrow = button.querySelector(".arrow");

  if (content.style.display === "block") {
    content.style.display = "none";
    arrow.classList.remove("rotate");
  } else {
    content.style.display = "block";
    arrow.classList.add("rotate");
  }
}

//Close menus when clicking outside
document.addEventListener("click", function (event) {
  const sideMenu = document.getElementById("sideMenu");
  const mobileMenu = document.getElementById("mobileMenu");
  const toggleBtn = document.querySelector(".menu-toggle");

  // If click is outside the menus & toggle button → close them
  if (
    !sideMenu.contains(event.target) &&
    !mobileMenu.contains(event.target) &&
    !toggleBtn.contains(event.target)
  ) {
    sideMenu.style.display = "none";
    mobileMenu.style.display = "none";
  }
});


document.addEventListener("DOMContentLoaded", () => {
  console.log("Web Development page loaded.");

  // Example: Smooth scroll to contact section when CTA clicked
  const ctaLink = document.querySelector(".cta a");
  if (ctaLink && ctaLink.getAttribute("href").startsWith("#")) {
    ctaLink.addEventListener("click", (e) => {
      e.preventDefault();
      document.querySelector(ctaLink.getAttribute("href")).scrollIntoView({ behavior: "smooth" });
    });
  }
});

document.addEventListener('DOMContentLoaded', function() {
    const container = document.querySelector('.container');
    const numberOfPixies = 30;

    for (let i = 0; i < numberOfPixies; i++) {
        const pixie = document.createElement('div');
        pixie.className = 'pixie';
        container.appendChild(pixie);

        const size = Math.random() * 5 + 2;
        pixie.style.width = `${size}px`;
        pixie.style.height = `${size}px`;
        pixie.style.left = `${Math.random() * 100}%`;
        pixie.style.top = `${Math.random() * 100}%`;
        pixie.style.animationDelay = `${Math.random() * 5}s`;
        pixie.style.animationDuration = `${Math.random() * 10 + 5}s`;
    }
});

let body = document.querySelector('body');

if (body != null) {
    if (body.style.overflow === 'hidden') {
        body.setAttribute('style', 'overflow: auto!important');
    }
}
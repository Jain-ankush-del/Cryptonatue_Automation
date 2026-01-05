let bounds = arguments[0].getBoundingClientRect();
let firstNavElement = document.getElementsByTagName('nav')[0];
let upperBound = (firstNavElement === undefined) ? 0 : firstNavElement.getBoundingClientRect().height;

return bounds.top >= upperBound
    && bounds.left >= 0
    && bounds.right <= (window.innerWidth || document.documentElement.clientWidth)
    && bounds.bottom <= (window.innerHeight || document.documentElement.clientHeight);
let rect = arguments[0].getBoundingClientRect();

return [Math.floor(rect.left + rect.right) / 2, Math.floor(rect.top + rect.bottom) / 2];
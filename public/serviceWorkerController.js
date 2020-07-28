if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('serviceWorker.js', { scope: '/' })
        .then(reg => {
            console.log('[PWA Info] registered serviceWorker.js', reg);
        })
        .catch(err => {
            console.log('[PWA Info] error registering serviceWorker.js', err);
        });
    })
} else {
    console.log('[PWA Info] browser does not support service workers');
}

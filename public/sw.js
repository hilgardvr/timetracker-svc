if (navigator.serviceWorker.controller) {
    console.log('[PWA Info] active service worker found, not registering')
} else {
    navigator.serviceWorker.register('serviceWorker.js', {
        scope: '/'
    }).then(reg => {
        console.log('[PWA Info] Service worker has been registered for scope:' + reg.scope);
    });
}
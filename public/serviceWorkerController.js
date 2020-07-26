window.onload = () => {
    console.log('[PWA Info] load serviceWorker.js');
    'use strict';

    if (navigator.serviceWorker !== undefined) {
        navigator.serviceWorker.register('serviceWorker.js', { scope: '/' })
        .then(reg => {
            console.log('[PWA Info] registered serviceWorker.js');
        })
        .catch(err => {
            console.log('[PWA Info] error registering serviceWorker.js', err);
        });
    } else {
        console.log('[PWA Info] serviceWorker already installed');
    }
}

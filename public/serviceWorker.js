const CACHE_NAME = "v0.1"

const FILES_TO_CACHE = [
    'offline.html'
]

// store offline page
self.addEventListener('install', event => {
    var offlineSite = new Request('offline.html');
    event.waitUntil(
        fetch(offlineSite).then(response => {
            return caches.open(CACHE_NAME)
                .then(cache => {
                    console.log('[PWA Info] Cached offline page')
                    return cache.put(offlineSite, response);
                });
        })
    );
})

//serve offline page if fetch fails
self.addEventListener('fetch', event => {
    event.respondWith(
        fetch(event.request).catch(error => {
            console.log('[PWA Info] App offline, serving offline page' + error);
            return caches.open('mypwa-offline').then(cache => {
                return cache.match('offline.html');
            })
        })
    );
});


self.addEventListener('refreshOffline', reponse => {
    return caches.open('mypwa-offline').then(cache => {
        console.log('[PWA Info] Offline page updated'); 
        return cache.put(offlineSite, response)
    })
})


// const CACHE_NAME = 'static-cache-v1';

// const FILES_TO_CACHE = [
//     '/',
//     '/assets/index.html',
//     '/assets/serviceWorker.js',
//     '/assets/manifest.json',
//     '/assets/javascript/main.js'
// ];


// self.addEventListener('activate', (evt) => {
//     console.log('[ServiceWorker] activate');
//     evt.waitUntil(
//         caches.keys().then((keyList) => {
//             return Promise.all(keyList.map((key) => {
//                 if (key !== CACHE_NAME && key != DATA_CACHE_NAME) {
//                     console.log('[ServiceWorker] Removing old cache', key);
//                     return caches.delete(key);
//                 }
//             }));
//         })
//     );
//     console.log('[ServiceWorker] Claim')
//     self.clients.claim();
// });

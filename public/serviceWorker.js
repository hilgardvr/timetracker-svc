const CACHE_NAME = "sttv0.1"

const FILES_TO_CACHE = [
    '/',
    '/assets/html/index.html',
    '/assets/images/clock.png',
    '/assets/javascripts/main.js',
    '/assets/manifest.json'
]

// store offline page
self.addEventListener('install', event => {
    event.waitUntil(
        caches
        .open(CACHE_NAME)
        .then(cache => {
            console.log('[PWA Info] Caching offline pages', FILES_TO_CACHE)
            return cache.addAll(FILES_TO_CACHE).catch(err => console.log('[PWA Info] Caching failed ', err));
        })
    );
})

//serve offline page if fetch fails
self.addEventListener('fetch', event => {
    event.respondWith(
        caches
        .match(event.request)
        .then(response => {
            console.log("[PWA Info] fetch: ", response)
            return response || fetch(event.request);
        })
    );
});


//serve offline page if fetch fails
// self.addEventListener('fetch', event => {
//     event.respondWith(
//         fetch(event.request).catch(error => {
//             console.log('[PWA Info] App offline, serving offline page' + error);
//             return caches.open('mypwa-offline').then(cache => {
//                 return cache.match('offline.html');
//             })
//         })
//     );
// });


// self.addEventListener('refreshOffline', reponse => {
//     return caches.open('mypwa-offline').then(cache => {
//         console.log('[PWA Info] Offline page updated'); 
//         return cache.put(offlineSite, response)
//     })
// })
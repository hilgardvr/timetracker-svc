const CACHE_NAME = "stt-v0.5"
const DATA_CACHE_NAME = "stt-data-v0.4"

const FILES_TO_CACHE = [
    '/',
    '/assets/html/index.html',
    '/assets/images/clock.png',
    '/assets/serviceWorkerController.js',
    '/assets/javascripts/main.js',
    '/assets/manifest.json'
]

// store offline page
self.addEventListener('install', event => {
    caches
        .open(CACHE_NAME)
        .then(cache => {
            console.log('[PWA Info] Caching offline pages', FILES_TO_CACHE)
            return cache.addAll(FILES_TO_CACHE).catch(err => console.log('[PWA Info] Caching failed ', err));
        })
    self.skipWaiting();
})

//clean up old data
self.addEventListener('activate', event => {
    console.log('[PWA Info] activate');
    event.waitUntil(
        caches.keys().then(keyList => {
            return Promise.all(keyList.map(key => {
                if (key !== CACHE_NAME && key !== DATA_CACHE_NAME) {
                    console.log('[PWA Info] removing old cache', key);
                    return caches.delete(key);
                }
            }));
        })
    )
    self.clients.claim();
})

//serve offline page if fetch fails
self.addEventListener('fetch', event => {
    if (event.request.url.includes('/userhistory/')) {
        console.log("[PWA Info] fetch (data): ", event.request.url)
        event.respondWith(
            caches.open(DATA_CACHE_NAME).then(cache => {
                return fetch(event.request)
                    .then( response => {
                        if (response.status === 200) {
                            cache.put(event.request.url, response.clone())
                        }
                        return response;
                    }).catch( err => {
                        return cache.match(event.request)
                    })
            })
        )
        return
    }
    event.respondWith(
        caches
        .match(event.request)
        .then(response => {
            console.log("[PWA Info] fetch response: ", response)
            return response || fetch(event.request);
        })
    );
});
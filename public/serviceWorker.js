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

//clean up old data
self.addEventListener('activate', event => {
    console.log('[PWA Info] activate');
    event.waitUntil(
        caches.keys().then(keyList => {
            return Promise.all(keyList.map(key => {
                if (key !== CACHE_NAME) {
                    console.log('[PWA Info] removing old cache', key);
                    return caches.delete(key);
                }
            }));
        })
    )
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
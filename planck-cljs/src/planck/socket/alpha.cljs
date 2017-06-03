(ns planck.socket.alpha
  "Planck socket functionality."
  (:require
   [cljs.spec.alpha :as s]
   [planck.repl :as repl]))

(s/def ::host string?)
(s/def ::port integer?)
(s/def ::data string?)                                      ; Maybe also byte arrays in the future?
(s/def ::socket integer?)
(s/def ::data-handler (s/fspec :args (s/cat :socket ::socket :data ::data) :ret nil?))
(s/def ::accept-handler (s/fspec :args (s/cat :socket ::socket) :ret ::data-handler))
(s/def ::opts (s/nilable map?))

(defn connect
  "Connects a TCP socket to a remote host/port. The connected socket reference
  is returned. Data can be written to the socket using `write` and the socket
  can be closed using `close`.

  A data-handler argument must be supplied, which is a function that accepts a
  socket reference and data. This data handler will be called when data arrives
  on the socket."
  ([host port data-handler]
   (connect host port data-handler nil))
  ([host port data-handler opts]
   (js/PLANCK_SOCKET_CONNECT host port data-handler)))

(s/fdef connect
  :args (s/cat :host ::host :port ::port :data-handler ::data-handler :opts (s/? ::opts))
  :ret ::socket)

(defn write
  "Writes data to a socket."
  ([socket data]
   (write socket data nil))
  ([socket data opts]
   (js/PLANCK_SOCKET_WRITE socket data)))

(s/fdef write
  :args (s/cat :socket ::socket :data ::data :opts (s/? ::opts)))

(defn close
  "Closes a socket."
  ([socket]
   (close socket nil))
  ([socket opts]
   (js/PLANCK_SOCKET_CLOSE socket)))

(s/fdef close
  :args (s/cat :socket ::socket :opts (s/? ::opts))
  :ret nil?)

(defn listen
  "Opens a server socket, listening for inbound connections. The port to
  listen on must be specified, along with an accept-handler, which should be a
  function that accepts a socket reference and returns a data handler. The data
  handler is a function that accepts a socket reference and some data.

  For example, an echo server could be written in this way:

    (listen 55555
      (fn [socket]
        (fn [socket data]
          (write socket data))))"
  ([port accept-handler]
   (listen port accept-handler nil))
  ([port accept-handler opts]
   (js/PLANCK_SOCKET_LISTEN port accept-handler)))

(s/fdef listen
  :args (s/cat :socket ::socket :accept-handler ::accept-handler :opts (s/? ::opts))
  :ret nil?)

(repl/register-speced-vars
  `connect
  `write
  `close
  `listen)
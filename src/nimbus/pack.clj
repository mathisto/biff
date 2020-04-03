(ns ^:nimbus nimbus.pack
  (:require
    [clojure.edn :as edn]
    [trident.util :as u]
    [taoensso.timbre :as timbre :refer [trace debug info warn error tracef debugf infof warnf errorf]]
    [nimbus.comms :refer [api-send api]]))

(def subscriptions (atom #{}))

(defmethod api ::subscribe
  [{:keys [uid admin] :as event} _]
  (when admin
    (swap! subscriptions conj uid)
    (api-send uid [::subscribe
                   {:query nil
                    :changeset {[::deps nil]
                                (edn/read-string (slurp "deps.edn"))}}]))
  nil)

(defmethod api ::fire
  [{:keys [admin] :as event} _]
  (u/pprint event)
  (println admin))

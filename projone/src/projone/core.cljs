(ns projone.core
  [:require-macros [cljs.core.async.macros :refer [go go-loop]]]
  [:require
   [reagent.core :as rgt]
   [cljs.core.async :as async :refer [put! >! <! chan]]
   [projone.qex :as qex]
   [projone.dancer :as dancer]])

(enable-console-print!)



(defonce app-state
  (rgt/atom
   {:message "hello marion, is that you?"
    :items [{:idx 1 :display "Item 1"}
            {:idx 2 :display "Item 2"}
            {:idx 3 :display "Item 3"}
            {:idx 4 :display "Item 4"}
            {:idx 5 :display "Item 5"}
            {:idx 6 :display "TEST"}]
    :active-item {}}))

(def curcnt (rgt/atom 0))

(def event-channel (chan))

(def events
  {:update-active-item
   (fn [{:keys [active-item]}]
     (swap! app-state assoc :active-item active-item))})


(go
  (while true
    (let [[event data :as x] (<! event-channel)]
      (println (pr-str x))
      ((events event) data))))


(defn inc-count []
  [:p {:on-click (fn[_] (swap! curcnt inc))}
   @curcnt])

(defn header [msg]
  [:div {}
   [:h2 {:class "title"} msg]])


;;; Was the on-click
#_(fn[_]
  (swap! app-state assoc :active-item item)
  (put! event-channel item ))
(defn item-list [event-channel items active-item]
  [:div {:class "item-list"}
   (for [item items]
     ^{:key (item :display)}
     [:div {:class (if (= item active-item) "item active" "item")}
      [:p
       {:on-click
        (fn[_]
          (put! event-channel [:update-active-item {:active-item item}]))}
       (item :display)]])])


(defn app []
  #_(swap! app-state assoc :items (subvec (@app-state :items) 0 5))
  #_(swap! app-state
         assoc :items (conj (@app-state :items) {:idx 6 :display "foo"}))
  [:div {:class "container"}
   [header (@app-state :message)]
   #_[inc-count]
   (item-list event-channel (@app-state :items) (@app-state :active-item))])

(rgt/render [app] (js/document.querySelector "#cljs-target"))




#_(js/setTimeout
 #(swap! app-state assoc-in [:message] "New message...")
 2000)

;;; (println "Hello, World!" (str js/React))
;;; (js/console.log js/React)
;;; (js/alert (str "Hi there ..., result is " (* 5 10)))

(defn say-hello
  "I don't do a whole lot."
  []
  (println (* 5 10)))



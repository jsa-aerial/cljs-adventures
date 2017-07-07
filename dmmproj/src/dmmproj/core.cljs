(ns dmmproj.core
  [:require-macros [cljs.core.async.macros :refer [go go-loop]]]
  [:require
   [reagent.core :as rgt]
   
   [dmm.core :as dmm]
   [dmmproj.widgets :refer [app]]

   [dmmproj.qex :as qex]
   [dmm.examples.jul-6-2017-experiment :as exp]
   #_[dmmproj.dancer :as dancer]

   ])

(enable-console-print!)





(rgt/render [app] (js/document.querySelector "#cljs-target"))


#_(js/setTimeout
 #(swap! app-state assoc-in [:message] "New message...")
 2000)

;;; (println "Hello, World!" (str js/React))
;;; (js/console.log js/React)
;;; (js/alert (str "Hi there ..., result is " (* 5 10)))

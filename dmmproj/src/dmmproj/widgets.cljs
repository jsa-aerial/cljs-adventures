(ns dmmproj.widgets
  [:require-macros [cljs.core.async.macros :refer [go go-loop]]]
  [:require
   [cljs.core.async :as async :refer [put! >! <! chan]]
   [ajax.core :as ajx]
   [reagent.core :as rgt]

   [re-com.core
    :as rcm
    :refer [h-box v-box box gap line
            button row-button md-icon-button md-circle-icon-button info-button
            input-text input-password input-textarea
            label title p
            checkbox radio-button slider progress-bar throbber
            horizontal-bar-tabs vertical-bar-tabs
            modal-panel popover-content-wrapper popover-anchor-wrapper]
    :refer-macros [handler-fn]]

   [dmmproj.widutils
    :refer [panel-title title2 href clicker status-text]]
   ])

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
    :active-item {}
    :frame-rate 1}))

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



(def icons
  [{:id "zmdi-plus"    :label [:i {:class "zmdi zmdi-plus"}]}
   {:id "zmdi-delete"  :label [:i {:class "zmdi zmdi-delete"}]}
   {:id "zmdi-undo"    :label [:i {:class "zmdi zmdi-undo"}]}
   {:id "zmdi-home"    :label [:i {:class "zmdi zmdi-home"}]}
   {:id "zmdi-account" :label [:i {:class "zmdi zmdi-account"}]}
   {:id "zmdi-info"    :label [:i {:class "zmdi zmdi-info"}]}])

(defn example-icons
  [selected-icon]
  [h-box
   :align :center
   :gap "8px"
   :children [[label :label "Choose an icon:"]
              [horizontal-bar-tabs
               :model     selected-icon
               :tabs      icons
               :on-change #(reset! selected-icon %)]
              [label :label @selected-icon]]])

(defn icon-button-test
  []
  (let [selected-icon (rgt/atom (:id (first icons)))]
    (fn []
      [v-box
       :gap "10px"
       :size "auto"
       :children
       [[example-icons selected-icon]
        [line]
        [h-box
         :align :center
         :gap "20px"
         :children
         [[box :width "90px" :child [:code ":size"]]
          [md-circle-icon-button
           :md-icon-name @selected-icon
           :tooltip      ":size set to :smaller"
           :size         :smaller
           :on-click #()]
          [md-circle-icon-button
           :md-icon-name @selected-icon
           :tooltip      "No :size set. This is the default button"
           :on-click     #()]
          [md-circle-icon-button
           :md-icon-name @selected-icon
           :tooltip      ":size set to :larger"
           :size         :larger
           :on-click #()]]]]])))


(defn inc-count []
  [:p {:on-click (fn[_] (swap! curcnt inc))}
   @curcnt])

(defn header [msg]
  [title2 msg :class "title" :style #_{:background-color "lightgreen"}])


;;; Was the on-click
#_(fn[_]
  (swap! app-state assoc :active-item item)
  (put! event-channel item ))
#_(defn item-list [event-channel items active-item]
  [:div {:class "item-list"}
   (for [item items]
     ^{:key (item :display)}
     [:div {:class (if (= item active-item) "item active" "item")}
      [:p
       {:on-click
        (fn[_]
          (put! event-channel [:update-active-item {:active-item item}]))}
       (item :display)]])])

(defn item-list [event-channel items active-item]
  [v-box :class "item-list"
   :size "auto"
   :gap "10px"
   :children
   (for [item items]
     ^{:key (item :display)}
     [box
      :class (if (= item active-item) "item active" "item")
      :attr {:on-click
             (fn[] (put! event-channel
                         [:update-active-item {:active-item item}]))}
      :child (item :display)])])


(defn frame-slider
  [n]
  (let [slider-val  (rgt/atom "10.0")
        slider-min  (rgt/atom "0.1")
        slider-max  (rgt/atom "100")
        slider-step (rgt/atom "0.1")
        disabled?   (rgt/atom false)]
    (fn[n]
      [h-box
       :align :center
       :gap "20px"
       :children [[label :label [:b "Frame Rate"]]
                  [slider
                   :model     slider-val
                   :min       slider-min
                   :max       slider-max
                   :step      slider-step
                   :width     "300px"
                   :on-change #(do (reset! slider-val (str %))
                                   (swap! app-state assoc :frame-rate %))
                   :disabled? disabled?]
                  [input-text
                   :model      slider-val
                   :width      "60px"
                   :height     "26px"
                   :on-change  #(do (reset! slider-val (str %))
                                    (swap! app-state assoc :frame-rate %))
                   :change-on-blur? false]]])))


;;To answer my own question:
;;view:
;;[:input {:type "file" :id "file" :name "file"
;;         :on-change
;;         #(dispatch [:save-rm-file :file (-> % .-target .-files (aget 0))])}]
;;in the handler:
;;(http/put url {:multipart-params {k file}})...


(defn app []
  #_(swap! app-state assoc :items (subvec (@app-state :items) 0 5))
  #_(swap! app-state
         assoc :items
         (conj (@app-state :items) {:idx 6 :display [:code "foo"]}))
  [h-box
   :gap "50px"
   :children
   [[gap :size "5px"]
    [v-box
     :gap "10px"
     :children
     [[header (@app-state :message)]
      [icon-button-test]
      ;;[inc-count]
      ;;[md-circle-icon-button-demo]
      [line]
      #_(item-list event-channel
                 (@app-state :items)
                 (@app-state :active-item))
      [frame-slider 1]
      [:canvas {:id "quil-canvas"}]
      ]]]])

#_(rgt/render [app] (js/document.querySelector "#cljs-target"))




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



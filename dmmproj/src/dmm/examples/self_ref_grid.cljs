(ns dmm.examples.self-ref-grid
  (:require
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]

   [cljs.core.async :as async :refer [put! >! <! chan]]
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
    :refer [panel-title title2 href clicker status-text]]))

(enable-console-print!)

(defonce app-state
  (rgt/atom
   {:message "hello marion, is that you?"
    :frame-rate 4
    :width 500
    :height 500

    }))


(defn setup []
  (q/frame-rate (@app-state :frame-rate)))

(defn update-state [state]
  (q/frame-rate (@app-state :frame-rate))
  :do-state-updates)

(defn draw-state [state]
  (q/background 127))


(q/defsketch selfie
  :host "quil-sketch"
  :size [(@app-state :width) (@app-state :height)]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:no-start]
  :middleware [m/fun-mode])


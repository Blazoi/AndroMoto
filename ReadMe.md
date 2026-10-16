<div align="center">
  <h1>🏍️ Android Moto</h1>
  <p><i>An Apple CarPlay / Android Auto style dashboard<br>built for navigation & music control.</i></p>

  <p>
    <b>By:</b> Jack Adam's Dieujuste &nbsp;|&nbsp; <b>Date:</b> October 2026
  </p>

  ---
</div>

<br/>

## Overview

I wanted to be able to easily control music and navigation **Apple CarPlay/Android Auto** style, but it wasn't available for your phone so I made my own.

<br/>

<div align="center">
  <h3>Current App State</h3>
  <img src="app/src/img/p1_v1.png" alt="Full Page Dashboard" width="700" style="border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"/>
</div>

<br/>

---

## Permissions

Before launching navigation, the app requires permission for **precise location access**.

<table width="100%">
  <tr>
    <td width="50%" align="center" valign="top">
      <h4>1. Request Permission</h4>
      <img src="app/src/img/loc_perm_request.jpg" alt="Permission Request" width="100%"/>
    </td>
    <td width="50%" align="center" valign="top">
      <h4>2. Location Access Denied</h4>
      <img src="app/src/img/loc_perm_denied.jpg" alt="Permission Denied" width="100%"/>
    </td>
  </tr>
</table>

> **Planned Update:** *Adjust permission handling so non-location features are available even if location access is denied.*

<br/>

---

## Core Components

For the navigation I used the **Google Navigation SDK** which gets you real-time turn by turn navigation. Essentially giving me a mini google maps running inside of my app.

<div align="center">
  <img src="app/src/img/p1_maps_v1.png" alt="Maps View" width="650" style="border-radius: 8px;"/>
</div>

<br/>

### Quick Access Bottom Bar
A dedicated control bar designed for fast, gloved touch interactions on the go:

<table width="100%">
  <tr>
    <td width="50%" align="center" valign="top">
      <img src="app/src/img/p1_qa_v1.jpg" alt="Waypoints Quick Access" width="100%"/>
      <br/><sub><b>Waypoints & Addresses</b></sub>
    </td>
    <td width="50%" align="center" valign="top">
      <img src="app/src/img/qa_ma_v1.jpg" alt="Maps Actions Quick Access" width="100%"/>
      <br/><sub><b>Maps Quick Actions</b></sub>
    </td>
  </tr>
</table>

* **Start Navigation:** Quick route to pre-saved favorite locations.
* **Save Addresses:** Add new destinations directly to your quick-start grid.
* **One-Time Search:** Search and navigate without saving to your grid.
* **Playlist Switcher:** Audio control on the move.
* **Map Controls:** Easily toggling maps settings

<br/>

### Music Widget
The music widget will use the **Spotify SDK/API** for access to my library and real time play-back control.

> **Future Update:** *When implemented, album artwork, song progress, name, artist, etc... will be dislayed properly*

<div align="center">
  <img src="app/src/img/spotify_element.jpg" alt="Spotify Widget" width="650" style="border-radius: 8px;"/>
</div>
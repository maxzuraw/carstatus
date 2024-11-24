<details open>
    <summary><h2>Table of contents</h2></summary>
<ul>
<li><a href="#overview">Overview</a></li>
<li><a href="#properties">Properties</a></li>
<ul>
    <li><a href="#insurance">Insurance</a></li>
    <li><a href="#TopGarage">TopGarage</a></li>
</ul>
</ul>
</details>

## Overview

This project implements requirements described in [Vehical Status Service](docs/Vehicle%20Status%20Service.md) file.


# Properties

## Insurance
``
carstatus.simulation.insurance.enabled
``

if `true` enables `InsuranceStub`, otherwise uses `Client`.

In tests it's set to `true`.

## TopGarage
``
carstatus.simulation.maintenance.enabled
``

if `true` enables `TopGarageStub`, otherwise uses `Client`.

In tests it's set to `true`.

# Run application

open terminal and run

``
mvn mn:run
``
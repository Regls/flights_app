import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ClientListComponent } from './clients/client-list/client-list.component';
import { CreateClientComponent } from './clients/create-client/create-client.component';
import { UpdateClientComponent } from './clients/update-client/update-client.component';
import { ClientDetailsComponent } from './clients/client-details/client-details.component';
import { AirportListComponent } from './airports/airport-list/airport-list.component';
import { CreateAirportComponent } from './airports/create-airport/create-airport.component';
import { UpdateAirportComponent } from './airports/update-airport/update-airport.component';
import { AirportDetailsComponent } from './airports/airport-details/airport-details.component';
import { AirlineListComponent } from './airlines/airline-list/airline-list.component';
import { CreateAirlineComponent } from './airlines/create-airline/create-airline.component';
import { UpdateAirlineComponent } from './airlines/update-airline/update-airline.component';
import { AirlineDetailsComponent } from './airlines/airline-details/airline-details.component';
import { FlightListComponent } from './flights/flight-list/flight-list.component';
import { CreateFlightComponent } from './flights/create-flight/create-flight.component';
import { FlightDetailsComponent } from './flights/flight-details/flight-details.component';

import { FormsModule} from '@angular/forms';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ClientListComponent,
    CreateClientComponent,
    UpdateClientComponent,
    ClientDetailsComponent,
    AirportListComponent,
    CreateAirportComponent,
    UpdateAirportComponent,
    AirportDetailsComponent,
    AirlineListComponent,
    CreateAirlineComponent,
    UpdateAirlineComponent,
    AirlineDetailsComponent,
    FlightListComponent,
    CreateFlightComponent,
    FlightDetailsComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

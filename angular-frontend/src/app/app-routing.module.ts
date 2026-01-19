import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
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
import { BookingListComponent } from './bookings/booking-list/booking-list.component';
import { CreateBookingComponent } from './bookings/create-booking/create-booking.component';

const routes: Routes = [
  {path: '', redirectTo: 'flightapp', pathMatch: 'full'},
  {path: 'flightapp', component: HomeComponent},

  {path: 'clients', component: ClientListComponent},
  {path: 'create-client', component: CreateClientComponent},
  {path: 'update-client/:id', component: UpdateClientComponent},
  {path: 'client-details/:id', component: ClientDetailsComponent},

  {path: 'airports', component: AirportListComponent},
  {path: 'create-airport', component: CreateAirportComponent},
  {path: 'update-airport/:id', component: UpdateAirportComponent},
  {path: 'airport-details/:id', component: AirportDetailsComponent},

  {path: 'airlines', component: AirlineListComponent},
  {path: 'create-airline', component: CreateAirlineComponent},
  {path: 'update-airline/:id', component: UpdateAirlineComponent},
  {path: 'airline-details/:id', component: AirlineDetailsComponent},

  {path: 'flights', component: FlightListComponent},
  {path: 'create-flight', component: CreateFlightComponent},
  {path: 'flight-details/:id', component: FlightDetailsComponent},

  {path: 'bookings', component: BookingListComponent},
  {path: 'create-booking', component: CreateBookingComponent},


  {path: '**', redirectTo: 'flightapp'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],                                                                                                                                                                                                                                                                                                          
  exports: [RouterModule]
})
export class AppRoutingModule { }

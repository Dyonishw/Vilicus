import React, {Component} from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom';

import Client from "./Client";

import AllItemsList from "./components/AllItemsList.js";
import CurrentShoppingList from "./components/CurrentShoppingList.js";

import './App.css';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {title: 'This is the Title in App.js',
                  };
  }

  render() {
    return (
    <div>
      <Router>
        <AllItemsList />
      </Router>

      <Router>
        <CurrentShoppingList />
      </Router>
    </div>

    );
  }
}
export default App;

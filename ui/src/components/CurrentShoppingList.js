import React, {Component} from 'react';
import {withRouter} from 'react-router';
import Cookies from 'js-cookie';

class CurrentShoppingList extends Component {

  constructor(props){
    super(props)

    this.state = {
      id: '',
      quantity: '',
      updateArray: [],
      receivedList: [],
      myToken: Cookies.get('MyCSRFToken')

    };

    this.handleChange = this.handleChange.bind(this);
    this.handleUpdate = this.handleUpdate.bind(this);
    this.handleFlush = this.handleFlush.bind(this);

  }

  componentDidMount() {

    fetch('/api/currentitems', {
      method: "GET",
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      }
    })
    .then((response) => response.json())
    .then(data => {
      this.setState({
        receivedList: data
      })
    })
  }

  handleChange(e) {
    const name = e.target.name;
    const value = e.target.value;
    this.setState({
      ...this.state,
      [name] : value
      });
  }

  handleUpdate(e) {

    e.preventDefault()

    const data = {
      "id": this.state.id,
      "quantity": this.state.quantity,
    }

    fetch('/api/updatecurrent', {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Csrf-Token': this.state.myToken
      }
    })
      .then(response => response.json())
      .then(data => {
        this.setState({
          receivedList: data
      })
    })
  }

  handleFlush(e) {
    e.preventDefault();

    fetch('/api/flush', {
      headers: {
        'Csrf-Token': this.state.myToken
      }
    })
      .then(response => response.json())
      .then(data => {
        this.setState({
          receivedList: data
        })
      })
  }

  render() {

    const tableReceivedList =
    <table>
      <tbody>
        <tr>
          <th>ID</th>
          <th>itemType</th>
          <th>itemSubType</th>
          <th>brand</th>
          <th>SKU</th>
          <th>quantity</th>
        </tr>
    {this.state.receivedList.map(lists =>
            <tr>
              <th key={lists.id}>{lists.id}</th>
              <th key={lists.id}>{lists.itemType}</th>
              <th key={lists.id}>{lists.itemSubType}</th>
              <th key={lists.id}>{lists.brand}</th>
              <th key={lists.id}>{lists.SKU}</th>
              <th key={lists.id}>{lists.quantity}</th>
            </tr>
    )}
      </tbody>
    </table>

    return (
    <div>
        <div>
        <p>
        These are all of your items
        </p>
          {tableReceivedList }
        </div>
      <div>
        <form onSubmit={this.handleUpdate}>
          <label>
            ID:
            <input
              name="id"
              type="number"
              value={this.state.id}
              onChange={this.handleChange} />
          </label>
          <br />
          <label>
            Quantity:
            <input
              name="quantity"
              type="number"
              value={this.state.quantity}
              onChange={this.handleChange} />
          </label>
              <input type="submit" value="Submit" />
        </form>
      </div>
      <div>
        <button onClick={this.handleFlush}>
        Flush current list
        </button>
      </div>
    </div>
    )
  }
}

export default withRouter(CurrentShoppingList);

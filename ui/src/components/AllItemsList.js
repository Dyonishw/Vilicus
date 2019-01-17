import React, {Component} from 'react';
import {withRouter} from 'react-router';

class AllItemsList extends Component {

  constructor(props){
    super(props)

    this.state = {
      newList: '',
      id: '',
      itemType: '',
      itemSubType: '',
      brand: '',
      SKU: '',
      quantity: '',
      receivedList: [],
      deleteId: '',
      stringList: "",
      deleteId: ''
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleCreateSubmit = this.handleCreateSubmit.bind(this);
    this.handleDeleteSubmit = this.handleDeleteSubmit.bind(this);
    this.handleRestore = this.handleRestore.bind(this);

  }

  componentDidMount() {

    fetch('/api/allitems')
    .then(response => response.json())
      .then(data => {
        this.setState({receivedList: data});
      })
      .then(body => {
      console.log(this.state.receivedList)
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

  handleCreateSubmit(e) {

    e.preventDefault()

    const data = {
      "id": this.state.id,
      "itemType": this.state.itemType,
      "itemSubType": this.state.itemSubType,
      "brand": this.state.brand,
      "SKU": this.state.SKU,
      "quantity": this.state.quantity,
    }

    fetch('/api/create' , {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
    .then(response => response.json())
    .then(data => {
        this.setState({
          receivedList: data
        })
     })
  }

  handleDeleteSubmit(e) {

    e.preventDefault();

    const data = {
      "deleteId": this.state.deleteId,
    }

    fetch('/api/delete' , {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
    }).then(response => response.json())
    .then(data => {
          this.setState({
           receivedList: data
           })
    })
  }

  handleRestore(e) {
    e.preventDefault();

    fetch('/api/restore' , {
      method: "GET",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
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
        <div>
        <p>
        These are all of your items
        </p>
          {tableReceivedList }
        </div>
        <form onSubmit={this.handleCreateSubmit}>
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
            itemType:
            <input
              name="itemType"
              type="text"
              value={this.state.itemType}
              onChange={this.handleChange} />
          </label>
          <br />
          <label>
            itemSubType:
            <input
              name="itemSubType"
              type="text"
              value={this.state.itemSubType}
              onChange={this.handleChange} />
          </label>
          <br />
          <label>
            brand:
            <input
            name="brand"
            type="text"
            value={this.state.brand}
            onChange={this.handleChange} />
          </label>
          <br />
          <label>
            SKU:
            <input
            name="SKU"
            type="text"
            value={this.state.SKU}
            onChange={this.handleChange} />
          </label>
          <br />
          <label>
            quantity:
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
        <form onSubmit={this.handleDeleteSubmit}>
          <label>
            Warning: This will delete the custom Item with ID:
            <input
            name="deleteId"
            type="number"
            value={this.state.deleteId}
            onChange={this.handleChange} />
          </label>
          <input type="submit" value="Submit" />
        </form>
      </div>
      <div>
        <button onClick={this.handleRestore}>
        Delete all custom items
        </button>
      </div>
    </div>

    )
  }
}

export default withRouter(AllItemsList);

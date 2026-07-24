import { Component } from "react";

class GetUser extends Component {
  constructor(props) {
    super(props);

    this.state = {
      user: null,
    };
  }

  async componentDidMount() {
    const response = await fetch("https://api.randomuser.me/");

    const data = await response.json();

    this.setState({
      user: data.results[0],
    });
  }

  render() {
    if (this.state.user === null) {
      return <h2>Loading...</h2>;
    }

    return (
      <div style={{ textAlign: "center" }}>
        <img src={this.state.user.picture.large} alt="User" />

        <h2>
          {this.state.user.name.title} {this.state.user.name.first}
        </h2>
      </div>
    );
  }
}

export default GetUser;

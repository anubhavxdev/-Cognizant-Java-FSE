import React, { Component } from "react";

class Posts extends Component {
  constructor(props) {
    super(props);

    this.state = {
      posts: [
        {
          id: 1,
          title: "Introduction to React",
          body: "React is a JavaScript library for building user interfaces.",
        },
        {
          id: 2,
          title: "Understanding Props",
          body: "Props allow data to flow from parent to child.",
        },
        {
          id: 3,
          title: "React State",
          body: "State stores data that changes over time.",
        },
      ],
    };
  }

  componentDidCatch(error, info) {
    alert("An error occurred while rendering the posts.");
    console.error(error, info);
  }

  render() {
    return (
      <div>
        <h1>Blogger App</h1>

        {this.state.posts.map((post) => (
          <div key={post.id}>
            <h2>{post.title}</h2>

            <p>{post.body}</p>

            <hr />
          </div>
        ))}
      </div>
    );
  }
}

export default Posts;

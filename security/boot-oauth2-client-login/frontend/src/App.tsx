import { useEffect, useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "./assets/vite.svg";
import heroImg from "./assets/hero.png";
import "./App.css";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName, setUserName] = useState("");
  const [message, setMessage] = useState("");

  const handleLogin = () => {
    location.href = "http://localhost:8080/oauth2/authorization/keycloak";
  };

  const handleLogout = () => {
    location.href = "http://localhost:8080/logout";
  };

  const sendMessage = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/messages", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          "X-XSRF-Token": document.cookie.split("; ")
            .find((row) => row.startsWith("XSRF-TOKEN="))
            ?.split("=")[1] || "",
        },
        body: JSON.stringify({ message: "Hello from React!" }),
      });
      if (response.ok) {
        const data = await response.json();
        setMessage(data.message);
      } else {
        setMessage("Failed to send message");
      }
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  useEffect(() => {
    let ignored = false;

    const checkLoginStatus = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/users/me", {
          credentials: "include",
        });
        if (!ignored && response.ok) {
          setIsLoggedIn(true);
          const userData = await response.json();
          setUserName(userData.name);
        }
      } catch (error) {
        console.error("Error checking login status:", error);
      }
    };

    checkLoginStatus();

    return () => {
      ignored = true;
    };
  }, []);

  return (
    <>
      <section id="center">
        <div className="hero">
          <img src={heroImg} className="base" width="170" height="179" alt="" />
          <img src={reactLogo} className="framework" alt="React logo" />
          <img src={viteLogo} className="vite" alt="Vite logo" />
        </div>
        <div>
          <h1>Get started</h1>
        </div>
        {isLoggedIn ? (
          <div>
            <p>Welcome back, {userName}!</p>
            <button className="counter" onClick={handleLogout}>
              Logout
            </button>
            <button className="counter" onClick={sendMessage}>
              Send Message
            </button>
            {message && <p>Response: {message}</p>}
          </div>
        ) : (
          <button className="counter" onClick={handleLogin}>
            Login
          </button>
        )}
      </section>
    </>
  );
}

export default App;

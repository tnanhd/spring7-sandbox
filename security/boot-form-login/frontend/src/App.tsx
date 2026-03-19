import { useState } from "react";
import Login from "./Login";
import { logout } from "./api/auth";
import { createMessage, getMessage } from "./api/message";
import type { AxiosError } from "axios";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [message, setMessage] = useState("");

  const handleSignOut = async () => {
    await logout();
    setIsLoggedIn(false);
    setMessage("");
  };

  const handleMessageAction = async (action: () => Promise<string>) => {
    try {
      const result = await action();
      setMessage(result);
    } catch (error) {
      const err = error as Error | AxiosError;
      setMessage(err.message);
    }
  };

  if (!isLoggedIn) {
    return <Login onSuccess={() => setIsLoggedIn(true)} />;
  }

  return (
    <>
      <section id="center">
        <div>
          <h1>Welcome back</h1>
        </div>
        <button className="sign-out" onClick={handleSignOut}>
          Sign out
        </button>
        <button
          className="create-message"
          onClick={() => handleMessageAction(createMessage)}
        >
          Create message
        </button>
        <button
          className="create-message"
          onClick={() => handleMessageAction(getMessage)}
        >
          Get message
        </button>
        {message && <p>{message}</p>}
      </section>
    </>
  );
}

export default App;

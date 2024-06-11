import { useState, useEffect } from "react";

const useAuth = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(null);
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    checkLoginStatus();
  }, []);

  const checkLoginStatus = async () => {
    try {
      const response = await fetch("/session", {
        method: "GET",
        credentials: "include",
      });

      if (response.ok) {
        const data = await response.json();
        // console.log(data);
        setIsLoggedIn(true);
        setUserData(data);
      } else {
        setIsLoggedIn(false);
        setUserData(null);
      }
    } catch (error) {
      console.error("Error checking login status:", error);
    }
  };

  const handleLogout = async () => {
    try {
      const response = await fetch("/logout", {
        method: "GET",
        credentials: "include",
      });
      if (response.ok) {
        setIsLoggedIn(false);
        setUserData(null);
      } else {
        console.error("Logout failed");
      }
    } catch (error) {
      console.error("Error logging out:", error);
    }
  };

  return { isLoggedIn, userData, handleLogout };
};

export default useAuth;

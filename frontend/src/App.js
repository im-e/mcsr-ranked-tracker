import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MinecraftHome from './pages/MinecraftHome';
import './App.css';

function App() {
  return (
    <Router basename={process.env.PUBLIC_URL}>
      <div className="App">
        <Routes>
          <Route path="/" element={<MinecraftHome />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;

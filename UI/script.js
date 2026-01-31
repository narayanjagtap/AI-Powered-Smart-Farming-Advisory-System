// Configuration
const API_BASE_URL = 'https://farmingadvisorysystem.up.railway.app/api/farming';

// State
let messageCount = 0;

// Utility Functions
function showAlert(message, type = 'info') {
    const container = document.getElementById('alertContainer');
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;
    container.appendChild(alert);
    setTimeout(() => alert.remove(), 5000);
}

function showLoading(show) {
    document.getElementById('loadingIndicator').classList.toggle('active', show);
}

function showTyping(show) {
    document.getElementById('typingIndicator').classList.toggle('active', show);
}

function disableInputs(disable) {
    document.getElementById('chatInput').disabled = disable;
    document.getElementById('sendBtn').disabled = disable;
    document.getElementById('solutionBtn').disabled = disable;
}

function getCurrentTime() {
    return new Date().toLocaleTimeString('en-US', { 
        hour: '2-digit', 
        minute: '2-digit' 
    });
}

function addMessage(content, type) {
    const container = document.getElementById('chatContainer');
    const emptyState = container.querySelector('.empty-state');
    if (emptyState) emptyState.remove();

    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    
    const messageContent = document.createElement('div');
    messageContent.className = 'message-content';
    messageContent.textContent = content;
    
    const messageTime = document.createElement('span');
    messageTime.className = 'message-time';
    messageTime.textContent = getCurrentTime();
    
    messageDiv.appendChild(messageContent);
    messageDiv.appendChild(messageTime);
    container.appendChild(messageDiv);
    container.scrollTop = container.scrollHeight;
    
    messageCount++;
}

// API Functions
async function initializeFarmer() {
    const name = document.getElementById('farmerName').value.trim();
    const email = document.getElementById('farmerEmail').value.trim();
    const problemType = document.getElementById('problemType').value;
    const language = document.getElementById('language').value;

    if (!name || !email || !problemType) {
        showAlert('Please fill all required fields', 'error');
        return;
    }

    showLoading(true);
    document.getElementById('startBtn').disabled = true;

    try {
        const response = await fetch(`${API_BASE_URL}/initialize`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include', // CRITICAL: Include session cookies
            body: JSON.stringify({
                farmerName: name,
                farmerEmail: email,
                problemType: problemType,
                preferredLanguage: language
            })
        });

        const data = await response.json();

        if (response.ok && data.status === 'success') {
            document.getElementById('farmerForm').classList.add('hidden');
            document.getElementById('chatSection').classList.add('active');
            
            document.getElementById('farmerInfo').innerHTML = `
                <p><strong>Name:</strong> ${name}</p>
                <p><strong>Email:</strong> ${email}</p>
                <p><strong>Problem:</strong> ${problemType}</p>
                <p><strong>Language:</strong> ${language}</p>
            `;
            
            addMessage(data.response, 'assistant');
            showAlert('Session started successfully! 🎉', 'success');
            document.getElementById('chatInput').focus();
        } else {
            showAlert(data.response || 'Failed to initialize', 'error');
            document.getElementById('startBtn').disabled = false;
        }
    } catch (error) {
        console.error('Error:', error);
        showAlert('Network error: ' + error.message, 'error');
        document.getElementById('startBtn').disabled = false;
    } finally {
        showLoading(false);
    }
}

async function sendMessage() {
    const input = document.getElementById('chatInput');
    const message = input.value.trim();

    if (!message) {
        showAlert('Please enter a message', 'error');
        return;
    }

    addMessage(message, 'user');
    input.value = '';
    
    disableInputs(true);
    showTyping(true);

    try {
        const response = await fetch(`${API_BASE_URL}/chat`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include', // CRITICAL: Include session cookies
            body: JSON.stringify({
                message: message
            })
        });

        const data = await response.json();
        showTyping(false);

        if (response.ok && data.status === 'success') {
            addMessage(data.response, 'assistant');
        } else {
            showAlert(data.response || 'Failed to get response', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showAlert('Network error: ' + error.message, 'error');
    } finally {
        showTyping(false);
        disableInputs(false);
        input.focus();
    }
}

async function getFinalSolution() {
    if (messageCount < 2) {
        showAlert('Please have a conversation first', 'error');
        return;
    }

    if (!confirm('Generate final summary and send to email?')) return;

    showLoading(true);
    disableInputs(true);

    try {
        const response = await fetch(`${API_BASE_URL}/get-final-solution`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include', // CRITICAL: Include session cookies
            body: JSON.stringify({})
        });

        const data = await response.json();

        if (response.ok && data.status === 'success') {
            showAlert('✅ ' + data.response, 'success');
            addMessage('📧 Final solution sent to your email!', 'assistant');
        } else {
            showAlert(data.response || 'Failed to generate solution', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showAlert('Network error: ' + error.message, 'error');
    } finally {
        showLoading(false);
        disableInputs(false);
    }
}

async function resetSession() {
    if (!confirm('Start new conversation? Current session will be lost.')) return;

    try {
        await fetch(`${API_BASE_URL}/reset`, {
            method: 'POST',
            credentials: 'include'
        });
        location.reload();
    } catch (error) {
        location.reload();
    }
}

function handleKeyPress(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

window.addEventListener('load', () => {
    console.log('✅ Smart Farming Advisory System Loaded');
    console.log('API Base:', API_BASE_URL);
});

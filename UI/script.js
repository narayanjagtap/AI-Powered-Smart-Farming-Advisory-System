// Configuration
const API_BASE_URL = 'http://127.0.0.1:8080/api/farming';

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

    

window.addEventListener('load', () => {
    console.log('✅ Smart Farming Advisory System Loaded');
    console.log('API Base:', API_BASE_URL);
});